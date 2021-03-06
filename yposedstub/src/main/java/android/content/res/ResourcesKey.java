package android.content.res;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class ResourcesKey {
    @Nullable
    public final String mResDir;
    
    @Nullable
    public final String[] mSplitResDirs;
    
    @Nullable
    public final String[] mOverlayDirs;
    
    @Nullable
    public final String[] mLibDirs;
    
    public final int mDisplayId;
    
    @NonNull
    public final Configuration mOverrideConfiguration;
    
    @NonNull
    public final CompatibilityInfo mCompatInfo;
    
    public ResourcesKey(@Nullable String resDir,
                        @Nullable String[] splitResDirs,
                        @Nullable String[] overlayDirs,
                        @Nullable String[] libDirs,
                        int displayId,
                        @Nullable Configuration overrideConfig,
                        @Nullable CompatibilityInfo compatInfo) {
        throw new RuntimeException("Stub!");
    }
    
}

