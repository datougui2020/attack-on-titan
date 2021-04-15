/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#ifndef ATTACK_ON_TITAN_ASSET_EXT_H
#define ATTACK_ON_TITAN_ASSET_EXT_H

enum AASSET_MODE {
    /** No specific information about how data will be accessed. **/
            _UNKNOWN = AASSET_MODE_UNKNOWN,
    /** Read chunks, and seek forward and backward. */
            RANDOM = AASSET_MODE_RANDOM,
    /** Read sequentially, with an occasional forward seek. */
            STREAMING = AASSET_MODE_STREAMING,
    /** Caller plans to ask for a read-only buffer with all data. */
            BUFFER = AASSET_MODE_BUFFER
};

bool AssetEnumerateFileType(AAssetManager *assetManager,
                            const char *type, std::vector<std::string> &files);

bool AssetReadFile(AAssetManager *assetManager,
                   std::string &name, std::vector<uint8_t> &buf);

AAsset *aasset_create(AAssetManager *aAssetManager, char *assetName, AASSET_MODE mode);

#endif // ATTACK_ON_TITAN_ASSET_EXT_H
