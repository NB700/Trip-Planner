# TripPlans - Android Vacation Planner
## 1. Project Overview
TripPlans is an Android application developed using Java, XML, Android Studio, the Android SDK, Gradle, and Room. It allows users to manage vacations and excursions, set date-based alerts, search vacations, generate reports, and share vacation details.  

## 2. Features
- Create, view, update, and delete vacations.
- Manage excursions associated with vacations.
- Search saved vacations.
- Set alerts for vacation start and end dates and excursion dates.
- Generate reports displaying saved vacations and their details.
- Share vacation information.  

## 3. Technologies Used
- Java and XML
- Android Studio and Android SDK
- Gradle
- Room persistence library  

## 4. Requirements
- Android Studio and Android SDK.
- Minimum Android version: API 26 (Android 8.0).
- Target SDK: API 34 (Android 14).
- Android Emulator or physical Android device running API 26 or higher.
- At least 8 GB of computer RAM recommended for the emulator.  

## 5. Project Setup
1. Open the TripPlans GitLab repository and copy the Clone with HTTPS URL.
2. Open Android Studio and select Get from VCS.
3. Paste the repository URL and click Clone.
4. Open the project and wait for Gradle synchronization to finish.
5. Select Build -> Clean Project, then build the project to verify successful compilation and Room configuration.  

## 6. Build and Run
### Run on an Emulator
1. Open Device Manager in Android Studio.
2. Create or select an emulator running API 26 or higher.
3. Start the emulator, select it as the target device, and click Run.
### Run on a Physical Device
1. Enable Developer Options and USB Debugging on the Android device.
2. Connect the device to your computer via USB and approve authorization if prompted.
3. Select the device in Android Studio and click Run.
### Generate an APK
1. Select Build -> Build Bundle / APK -> Build APK.
2. Once complete, click Locate in the build notification.  

The debug APK is typically generated at app/build/outputs/apk/debug/app-debug.apk.
For distribution, use Build -> Generate Signed Bundle / APK to create a signed release APK.  

## 7. User Guide
TripPlans allows users to manage vacations and excursions, set alerts, manage notifications, share vacation details, and generate reports.  

### Getting Started
1. On the main page, click View Vacations to access the Vacations page.
2. Click the + button to add a vacation.
3. Enter the vacation title, hotel name, start date, and end date. The end date must be after the start date.
4. Open the top-right menu and select Save Vacation.
### Managing Vacations
- View or edit: Select a vacation from the Vacations page. Update its details and save your changes.
- Search: Select Vacation or Hotel from the drop-down menu and enter text in the search field.
- Delete: Select Delete Vacation from the vacation details menu. Vacations with excursions cannot be deleted.
- Share: Select Share Details to share vacation information through available options, such as email or text messaging.
### Managing Excursions
- Open a saved vacation and click the + button to add an excursion.
- Enter the excursion title and date.
- Select Save Excursion from the top-right menu.  

Select an excursion from the vacation details page to view or edit it. Use the excursion menu to save changes, delete the excursion, or set an alert.  

### Alerts and Notifications
- Vacation alerts: Open a vacation and select Alert from the menu to set alerts for its start and end dates.
- Excursion alerts: Open an excursion and select Alert to set an alert for its date.
- Device permissions: On the first attempt to set an alert, follow the device settings prompts to allow alarms. Save the vacation or excursion before setting alerts.
- Notifications: On the Vacations page, select Manage Notifications from the menu. Allow notification permissions when prompted, then enable or disable notifications in the application settings.  

### Reports
On the Vacations page, open the top-right menu and select Create Report to navigate to the Report page.  

### Input Requirements
Vacation titles, hotel names, and excursion titles are limited to 50 characters. These fields accept letters, numbers, spaces, periods, apostrophes, quotation marks, hyphens, and parentheses. Other characters are ignored.

## 8. Data Storage Notes
- Data is stored locally using Room.
- An internet connection is not required.
- Cloud synchronization is not supported.
- Clearing app data or uninstalling the application permanently deletes stored vacations and excursions.
