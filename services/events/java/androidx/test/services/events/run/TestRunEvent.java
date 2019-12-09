/*
 * Copyright (C) 2019 The Android Open Source Project
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

package androidx.test.services.events.run;

import android.os.Parcel;
import android.os.Parcelable;

/** Base class for all other {@code TestRunEvents} to extend. */
public abstract class TestRunEvent implements Parcelable {
  /** Creates a {@link TestRunEvent}. */
  TestRunEvent() {}

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(instanceType());
  }

  /**
   * The {@link ITestRunEvent#send(TestRunEvent)} service method receives an instance of the {@link
   * TestRunEvent} base class, so the {@link #CREATOR} factory in this class is being used to create
   * the event instances, not the {@code CREATOR} of one of its derived instances.
   *
   * <p>Therefore the {@code createFromParcel} method first needs to read a String containing the
   * class name of the correct derived type to instantiate. Derived classes should override this
   * method to return the full class name of that event type.
   *
   * <p>Also note that this means only this base class provides a {@code CREATOR}, since the derived
   * classes don't need one.
   *
   * @return the result of {@code getClass().getName()} of the final derived event class that
   *     extends this base class
   */
  abstract String instanceType();

  public static final Parcelable.Creator<TestRunEvent> CREATOR =
      new Parcelable.Creator<TestRunEvent>() {
        @Override
        public TestRunEvent createFromParcel(Parcel source) {
          String instanceType = source.readString();
          if (TestAssumptionFailureEvent.class.getName().equals(instanceType)) {
            return new TestAssumptionFailureEvent(source);
          } else if (TestFailureEvent.class.getName().equals(instanceType)) {
            return new TestFailureEvent(source);
          } else if (TestFinishedEvent.class.getName().equals(instanceType)) {
            return new TestFinishedEvent(source);
          } else if (TestIgnoredEvent.class.getName().equals(instanceType)) {
            return new TestIgnoredEvent(source);
          } else if (TestRunFinishedEvent.class.getName().equals(instanceType)) {
            return new TestRunFinishedEvent(source);
          } else if (TestRunStartedEvent.class.getName().equals(instanceType)) {
            return new TestRunStartedEvent(source);
          } else if (TestStartedEvent.class.getName().equals(instanceType)) {
            return new TestStartedEvent(source);
          } else {
            throw new IllegalStateException("Unrecognized instance type: " + instanceType);
          }
        }

        @Override
        public TestRunEvent[] newArray(int size) {
          return new TestRunEvent[size];
        }
      };
}
