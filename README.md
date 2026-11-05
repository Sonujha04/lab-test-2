# lab-test-2

# 🌍 DestinationGuide – Travel Information Android Application

## 📌 Project Overview

**DestinationGuide** is an Android application developed to provide users with useful travel information about different destinations around the world.

The application allows users to search for any destination by entering its name. It fetches real-time destination information from the **Wikipedia REST API** using HTTP requests and displays the destination's title, image, and a short description.

Users can also click the **Read More** button to explore the complete Wikipedia article directly inside the application using Android WebView.

The application includes WebView navigation features such as Back, Forward, Refresh, and Home, allowing users to browse articles without leaving the application.

This project demonstrates the practical implementation of Android WebView, HTTP Requests, REST APIs, JSON Parsing, ImageView, and Web Services.

---

## 🎯 Objectives

The main objectives of this project are:

- To develop an Android application that provides travel information.
- To integrate a public REST API for fetching destination details.
- To understand HTTP GET requests and web service communication.
- To retrieve and parse JSON responses.
- To display destination images using ImageView.
- To implement WebView for displaying complete web articles.
- To implement Back, Forward, Refresh, and Home navigation controls.
- To display loading indicators using ProgressBar.
- To improve understanding of Android application development and API integration.

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Android Studio | Application development IDE |
| Java | Application programming language |
| XML | User interface design |
| Wikipedia REST API | Fetching destination information |
| HTTP GET Request | Communicating with the API |
| JSON Parsing | Extracting API response data |
| ImageView | Displaying destination images |
| WebView | Displaying complete Wikipedia articles |
| WebViewClient | Handling navigation inside the app |
| ProgressBar | Showing loading progress |
| Internet Permission | Accessing online services |

---

## 🌐 API Integration

This application uses the official **Wikipedia REST API** to retrieve destination information.

### API Base URL

```text
https://en.wikipedia.org/api/rest_v1/page/summary/
```

### Example API Request

```text
https://en.wikipedia.org/api/rest_v1/page/summary/India
```

### Other Supported Destinations

```text
https://en.wikipedia.org/api/rest_v1/page/summary/Paris

https://en.wikipedia.org/api/rest_v1/page/summary/Tokyo

https://en.wikipedia.org/api/rest_v1/page/summary/London

https://en.wikipedia.org/api/rest_v1/page/summary/New_Delhi
```

The destination name is dynamically appended to the API base URL based on the user's search input.

**API Key:** Not required.

### JSON Response Handling

The application receives a JSON response from the Wikipedia REST API.

The following JSON fields are extracted:

| JSON Field | Description |
|---|---|
| `title` | Name of the destination |
| `description` | Short destination description |
| `extract` | Detailed summary of the destination |
| `thumbnail.source` | URL of the destination image |
| `content_urls.desktop.page` | Complete Wikipedia article URL |

The extracted information is displayed on the destination summary screen.

---

## ✨ Main Features

### 1. Destination Search

- Users can enter any destination name.
- Search button initiates an HTTP GET request.
- Destination information is fetched dynamically from Wikipedia.
- Invalid or unavailable destinations are handled with suitable error messages.

### 2. Destination Summary

After searching, the application displays:

- Destination title.
- Destination image.
- Short description.
- Read More button.

The information is dynamically populated using the JSON response received from the API.

### 3. Read More Functionality

- Opens the complete Wikipedia article.
- Uses Android WebView.
- Displays the article inside the application.
- Does not redirect users to an external browser.

### 4. WebView Navigation

The application provides four navigation controls.

| Button | Function |
|---|---|
| Back | Moves to the previously visited webpage |
| Forward | Moves to the next webpage |
| Refresh | Reloads the current webpage |
| Home | Returns to the main search screen |

**Navigation Implementation:**

- `webView.goBack()` – Moves backward.
- `webView.goForward()` – Moves forward.
- `webView.reload()` – Refreshes the current webpage.
- `finish()` or appropriate navigation logic – Returns to the home screen.

The Back and Forward buttons are enabled or disabled depending on the WebView browsing history.

### 5. ProgressBar

- Displays a loading indicator while fetching destination information.
- Shows loading progress while opening a webpage.
- Disappears when loading is completed.
- Helps users understand that the application is processing their request.

### 6. Error Handling

The application handles:

- Empty search input.
- Invalid destination names.
- Internet connectivity problems.
- API request failures.
- Missing destination images.
- WebView loading errors.

---

## 🔄 Application Workflow

The application follows the workflow below:

1. User launches the DestinationGuide application.
2. The home screen displays a destination search field.
3. User enters a destination name.
4. User clicks the Search button.
5. The application sends an HTTP GET request to the Wikipedia REST API.
6. The API returns destination information in JSON format.
7. The application parses the JSON response.
8. Destination title, image, and description are displayed.
9. User clicks the Read More button.
10. The complete Wikipedia article opens inside WebView.
11. User can navigate using Back, Forward, Refresh, and Home buttons.

---

## 🏗️ Application Architecture

```text
              DESTINATIONGUIDE
                      |
                      |
                 Home Screen
                      |
                      |
              Enter Destination
                      |
                      |
                 Search Button
                      |
                      |
               HTTP GET Request
                      |
                      |
             Wikipedia REST API
                      |
                      |
                JSON Response
                      |
                      |
                 JSON Parsing
                      |
             ________________
             |              |
             |              |
         Title/Image     Description
             |              |
             |______________|
                      |
                      |
              Destination Summary
                      |
                      |
                 Read More
                      |
                      |
                  WebView
                      |
             _________________
             |       |       |
            Back  Forward  Refresh
                      |
                     Home
```

---

## 📂 Project Structure

The Android project contains the following important files:

```text
DestinationGuide/
│
├── app/
│   │
│   ├── src/
│   │   └── main/
│   │       │
│   │       ├── java/
│   │       │   └── your/package/name/
│   │       │       └── MainActivity.java
│   │       │
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml
│   │       │   │
│   │       │   ├── drawable/
│   │       │   └── mipmap/
│   │       │
│   │       └── AndroidManifest.xml
│   │
│   └── build.gradle
│
└── README.md
```

*Note: Additional Java classes and XML layouts may be included depending on the final implementation.*

---

## 🔐 Permissions

The application requires internet access to communicate with the Wikipedia REST API and load online articles.

Add the following permission inside `AndroidManifest.xml`, outside the application tag:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

---

## ⚙️ Installation and Execution

Follow these steps to run the project:

### Step 1: Clone the Repository

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

### Step 2: Open Android Studio

- Launch Android Studio.
- Select Open.
- Choose the downloaded DestinationGuide project folder.

### Step 3: Gradle Synchronization

- Allow Android Studio to synchronize Gradle.
- Wait until the project dependencies are downloaded.
- Resolve any build errors if they occur.

### Step 4: Run the Application

- Connect an Android device or start an emulator.
- Enable USB debugging if using a physical device.
- Click the Run button in Android Studio.

### Step 5: Test the Application

- Enter a destination such as India, Paris, or Tokyo.
- Click Search.
- Verify the destination information.
- Click Read More.
- Test Back, Forward, Refresh, and Home navigation.

---

## 🧪 Testing

The application can be tested using the following test cases:

| Test Case | Expected Result |
|---|---|
| Search for India | Displays India information |
| Search for Paris | Displays Paris information |
| Search for Tokyo | Displays Tokyo information |
| Empty search | Displays validation message |
| Invalid destination | Displays error message |
| Click Read More | Opens article inside WebView |
| Click Back | Navigates to previous page |
| Click Forward | Navigates to next page |
| Click Refresh | Reloads current page |
| Click Home | Returns to search screen |
| Disable internet | Displays suitable error message |

---

## 📸 Screenshots


<img width="370" height="801" alt="Screenshot 2026-11-05 105631" src="https://github.com/user-attachments/assets/56b77251-5384-4f00-9142-a7ecc3b39fe4" />

<img width="362" height="792" alt="Screenshot 2026-11-05 105730" src="https://github.com/user-attachments/assets/6ac0b89f-06f5-4b39-861d-50c19fc1cc3b" />


<img width="361" height="792" alt="Screenshot 2026-11-05 105741" src="https://github.com/user-attachments/assets/7e3ba836-273d-489a-a88b-4182e272be28" />


<img width="365" height="750" alt="Screenshot 2026-11-05 105756" src="https://github.com/user-attachments/assets/da9c2479-1dc8-42fa-ad6f-fc3ce8f42456" />




## 🚀 Future Enhancements

The application can be improved by adding:

- Search history for previously visited destinations.
- Favourite destinations and bookmarks.
- Google Maps integration for destination locations.
- Weather information for selected destinations.
- Travel recommendations and popular tourist attractions.
- Improved offline error handling.
- More attractive UI and animations.
- Multiple language support.

---

## 📚 Learning Outcomes

Through this project, the following concepts are demonstrated:

1. Understanding Android application development using Java.
2. Designing user interfaces using XML.
3. Integrating REST APIs into Android applications.
4. Performing HTTP GET requests.
5. Parsing JSON responses.
6. Displaying images dynamically using ImageView.
7. Implementing Android WebView.
8. Managing WebView navigation history.
9. Using ProgressBar for loading indicators.
10. Handling network errors and application navigation.

---

## 🔗 References

- **Wikipedia REST API:** https://en.wikipedia.org/api/rest_v1/
- **Wikipedia:** https://www.wikipedia.org/
- **Android Developers:** https://developer.android.com/
- **Android WebView Documentation:** https://developer.android.com/develop/ui/views/layout/webapps/webview
- **Android JSON Documentation:** https://developer.android.com/reference/org/json/JSONObject


This project is developed for educational and academic purposes.

Destination information and images are retrieved from Wikipedia. Please follow Wikimedia's applicable attribution and licensing requirements when redistributing their content.
