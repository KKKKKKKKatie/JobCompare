# JobCompare6300

JobCompare6300 is an Android application for comparing a user's current job against saved job offers. It lets the user enter compensation and location details, assign preference weights to compensation factors, rank jobs from best to worst, and compare two jobs side by side.

This project appears to have been built for Georgia Tech's CS 6300 course and is implemented as a single-user Android app with local SQLite storage.

## Features

- Add or edit the current job
- Add one or more job offers
- Adjust comparison weights for each compensation factor
- Rank saved jobs using a weighted score
- Compare any two saved jobs in a detailed side-by-side view

## Job Data Captured

Each job record includes:

- Title
- Company
- Location (city, state, cost-of-living index)
- Yearly salary
- Yearly bonus
- Retirement match percentage
- Restricted stock
- Personalized learning and development budget
- Family planning assistance

## Ranking Formula

Jobs are ranked using a weighted average based on user-configured comparison settings.

`AYS + AYB + (R * AYS / 100) + (RSUA / 3) + PLD + FPA`

Where:

- `AYS` = yearly salary adjusted for cost of living
- `AYB` = yearly bonus adjusted for cost of living
- `R` = retirement match percentage
- `RSUA` = restricted stock
- `PLD` = personalized learning and development budget
- `FPA` = family planning assistance

Weights for each factor can be set from `0` to `9`. If all weights are set to `0`, the app falls back to equal weighting.

## Tech Stack

- Android application
- Java 17
- Gradle
- SQLite via `SQLiteOpenHelper`
- JUnit 4
- Robolectric
- AndroidX test libraries

## Project Structure

- `app/src/main/java/edu/gatech/seclass/jobcompare6300/controller` - Android activities and screen flow
- `app/src/main/java/edu/gatech/seclass/jobcompare6300/model` - domain models
- `app/src/main/java/edu/gatech/seclass/jobcompare6300/database` - SQLite persistence layer
- `app/src/main/java/edu/gatech/seclass/jobcompare6300/util` - scoring and calculation utilities
- `app/src/test` - local unit and Robolectric tests
- `app/src/androidTest` - instrumentation tests
- `design-description.md` - detailed design writeup and requirement mapping

## Requirements

To build and run the project locally, you will need:

- Android Studio with Android SDK 33 installed
- JDK 17
- A device or emulator running Android 13+ because the app currently uses `minSdk 33`

## Getting Started

1. Clone the repository.
2. Open the project in Android Studio.
3. Let Gradle sync complete.
4. Run the `app` configuration on an emulator or Android device.

## Build and Test

From the project root:

```bash
./gradlew assembleDebug
./gradlew test
```

If you want to run instrumentation tests on a connected device or emulator:

```bash
./gradlew connectedAndroidTest
```

## Notes

- The app is designed around a single local user.
- Comparison settings are initialized with default weights of `1`.
- Data is stored locally in an SQLite database named `JobComparison`.

