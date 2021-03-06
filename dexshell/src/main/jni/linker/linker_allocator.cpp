#include "linker_allocator.h"
#include "extra_defines.h"

#include <inttypes.h>
#include <sys/mman.h>
#include <sys/prctl.h>
#include <unistd.h>
#include <cstring>

struct LinkerAllocatorPage {
  LinkerAllocatorPage* next;
  uint8_t bytes[PAGE_SIZE-sizeof(LinkerAllocatorPage*)];
};

struct FreeBlockInfo {
  void* next_block;
  size_t num_free_blocks;
};

LinkerBlockAllocator::LinkerBlockAllocator(size_t block_size)
  : block_size_(block_size < sizeof(FreeBlockInfo) ? sizeof(FreeBlockInfo) : block_size),
    page_list_(nullptr),
    free_block_list_(nullptr)
{}

void* LinkerBlockAllocator::alloc() {
  if (free_block_list_ == nullptr) {
    create_new_page();
  }

  FreeBlockInfo* block_info = reinterpret_cast<FreeBlockInfo*>(free_block_list_);
  if (block_info->num_free_blocks > 1) {
    FreeBlockInfo* next_block_info = reinterpret_cast<FreeBlockInfo*>(
      reinterpret_cast<char*>(free_block_list_) + block_size_);
    next_block_info->next_block = block_info->next_block;
    next_block_info->num_free_blocks = block_info->num_free_blocks - 1;
    free_block_list_ = next_block_info;
  } else {
    free_block_list_ = block_info->next_block;
  }

  memset(block_info, 0, block_size_);

  return block_info;
}

void LinkerBlockAllocator::free(void* block) {
  if (block == nullptr) {
    return;
  }

  LinkerAllocatorPage* page = find_page(block);

  if (page == nullptr) {
    abort();
  }

  ssize_t offset = reinterpret_cast<uint8_t*>(block) - page->bytes;

  if (offset % block_size_ != 0) {
    abort();
  }

  memset(block, 0, block_size_);

  FreeBlockInfo* block_info = reinterpret_cast<FreeBlockInfo*>(block);

  block_info->next_block = free_block_list_;
  block_info->num_free_blocks = 1;

  free_block_list_ = block_info;
}

void LinkerBlockAllocator::protect_all(int prot) {
  for (LinkerAllocatorPage* page = page_list_; page != nullptr; page = page->next) {
    if (mprotect(page, PAGE_SIZE, prot) == -1) {
      abort();
    }
  }
}

void LinkerBlockAllocator::create_new_page() {
  LinkerAllocatorPage* page = reinterpret_cast<LinkerAllocatorPage*>(mmap(nullptr, PAGE_SIZE,
      PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, 0, 0));
  if (page == MAP_FAILED) {
    abort(); // oom
  }

  prctl(PR_SET_VMA, PR_SET_VMA_ANON_NAME, page, PAGE_SIZE, "linker_alloc");

  memset(page, 0, PAGE_SIZE);

  FreeBlockInfo* first_block = reinterpret_cast<FreeBlockInfo*>(page->bytes);
  first_block->next_block = free_block_list_;
  first_block->num_free_blocks = (PAGE_SIZE - sizeof(LinkerAllocatorPage*))/block_size_;

  free_block_list_ = first_block;

  page->next = page_list_;
  page_list_ = page;
}

LinkerAllocatorPage* LinkerBlockAllocator::find_page(void* block) {
  if (block == nullptr) {
    abort();
  }

  LinkerAllocatorPage* page = page_list_;
  while (page != nullptr) {
    const uint8_t* page_ptr = reinterpret_cast<const uint8_t*>(page);
    if (block >= (page_ptr + sizeof(page->next)) && block < (page_ptr + PAGE_SIZE)) {
      return page;
    }

    page = page->next;
  }

  abort();
}
