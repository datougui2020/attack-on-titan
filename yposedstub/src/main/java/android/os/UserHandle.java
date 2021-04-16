package android.os;

/**
 * Copyright ® $ 2021
 * All right reserved.
 *
 * @author: jamesfchen
 * @email: hawksjamesf@gmail.com
 * @since: 四月/16/2021  星期五
 */
public final class UserHandle implements Parcelable {
    protected UserHandle(Parcel in) {
        throw new RuntimeException("Stub!");
    }

    public static final Creator<UserHandle> CREATOR = new Creator<UserHandle>() {
        @Override
        public UserHandle createFromParcel(Parcel in) {
            throw new RuntimeException("Stub!");
        }

        @Override
        public UserHandle[] newArray(int size) {
            throw new RuntimeException("Stub!");
        }
    };

    @Override
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        throw new RuntimeException("Stub!");
    }

    public static int getCallingUserId() {
        throw new RuntimeException("Stub!");
    }
}
