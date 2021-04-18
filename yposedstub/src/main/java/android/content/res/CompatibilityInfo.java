package android.content.res;

import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;

public class CompatibilityInfo implements Parcelable {
    
    public CompatibilityInfo(ApplicationInfo appInfo, int screenLayout, int sw,
                             boolean forceCompat) {
        throw new RuntimeException("Stub!");
    }

    private CompatibilityInfo() {
        throw new RuntimeException("Stub!");
    }
    public static final CompatibilityInfo DEFAULT_COMPATIBILITY_INFO = new CompatibilityInfo() {};
    @Override
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }
    
    public static final Creator<CompatibilityInfo> CREATOR
        = new Creator<CompatibilityInfo>() {
        @Override
        public CompatibilityInfo createFromParcel(Parcel source) {
            throw new RuntimeException("Stub!");
        }
        
        @Override
        public CompatibilityInfo[] newArray(int size) {
            throw new RuntimeException("Stub!");
        }
    };
    
}

