/*
 * Copyright (C) 2008 The Android Open Source Project
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
 */

package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;

public class ApplicationErrorReport implements Parcelable {

    protected ApplicationErrorReport(Parcel in) {
        throw new RuntimeException("Stub!");
    }

    public static final Creator<ApplicationErrorReport> CREATOR = new Creator<ApplicationErrorReport>() {
        @Override
        public ApplicationErrorReport createFromParcel(Parcel in) {
            throw new RuntimeException("Stub!");
        }

        @Override
        public ApplicationErrorReport[] newArray(int size) {
            throw new RuntimeException("Stub!");
        }
    };

    public static ComponentName getErrorReportReceiver(Context context,
                                                       String packageName, int appFlags) {
        throw new RuntimeException("Stub!");
    }


    static ComponentName getErrorReportReceiver(PackageManager pm, String errorPackage,
            String receiverPackage) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void readFromParcel(Parcel in) {
        throw new RuntimeException("Stub!");
    }
    public static class CrashInfo {

        public CrashInfo() {
            throw new RuntimeException("Stub!");
        }

        /**
         * Create an instance of CrashInfo initialized from an exception.
         */
        public CrashInfo(Throwable tr) {
            throw new RuntimeException("Stub!");

        }

        /** {@hide} */
        public void appendStackTrace(String tr) {
            throw new RuntimeException("Stub!");
        }

        /**
         * Ensure that the string is of reasonable size, truncating from the middle if needed.
         */
        private String sanitizeString(String s) {
            throw new RuntimeException("Stub!");
        }

        /**
         * Create an instance of CrashInfo initialized from a Parcel.
         */
        public CrashInfo(Parcel in) {
            throw new RuntimeException("Stub!");
        }

        /**
         * Save a CrashInfo instance to a parcel.
         */
        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        /**
         * Dump a CrashInfo instance to a Printer.
         */
        public void dump(Printer pw, String prefix) {
            throw new RuntimeException("Stub!");
        }
    }
    public static class ParcelableCrashInfo extends CrashInfo implements Parcelable {
        /**
         * Create an uninitialized instance of CrashInfo.
         */
        public ParcelableCrashInfo() {
            throw new RuntimeException("Stub!");
        }

        /**
         * Create an instance of CrashInfo initialized from an exception.
         */
        public ParcelableCrashInfo(Throwable tr) {
            throw new RuntimeException("Stub!");

        }

        public ParcelableCrashInfo(Parcel in) {
            throw new RuntimeException("Stub!");
        }

        public int describeContents() {
            throw new RuntimeException("Stub!");
        }

        public static final Parcelable.Creator<ParcelableCrashInfo> CREATOR =
                new Parcelable.Creator<ParcelableCrashInfo>() {
                    @Override
                    public ParcelableCrashInfo createFromParcel(Parcel in) {
                        throw new RuntimeException("Stub!");
                    }

                    @Override
                    public ParcelableCrashInfo[] newArray(int size) {
                        throw new RuntimeException("Stub!");
                    }
                };
    }
}
