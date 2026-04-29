# Weather App

## Overview

This project is a JavaFX Weather Information App that retrieves real-time
weather data and a short-term forecast for a selected city. The application
uses the OpenWeather API and presents the results in a graphical interface.

## Features

- Search weather information by city name
- Display current temperature, feels-like temperature, humidity, wind speed,
  minimum temperature, maximum temperature, and condition text
- Display weather icons for current conditions and forecast entries
- Show a three-day short-term forecast
- Switch temperature units between Celsius and Fahrenheit
- Switch wind-speed units between meters per second and miles per hour
- Show recent search history with timestamps
- Change theme based on morning, day, evening, and night

## API Key Configuration

This application requires a valid OpenWeatherMap API key. Paste your API key inside the `API_KEY` file. This project does not uses environment variables.

Visit: https://openweathermap.org/
Create a free account and generate your API key.

## Project Structure
```text
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── src
    └── main
        ├── java
        │   ├── app
        │   │   └── weather
        │   │       ├── App.java
        │   │       ├── controller
        │   │       │   └── WeatherController.java
        │   │       ├── model
        │   │       │   ├── ForecastData.java
        │   │       │   └── WeatherData.java
        │   │       ├── service
        │   │       │   └── WeatherService.java
        │   │       └── util
        │   │           ├── TimeUtils.java
        │   │           └── UnitConverter.java
        │   └── module-info.java
        └── resources
            ├── API_KEY
            └── view
                ├── style.css
                └── weather_view.fxml
```

## How to Run

### Requirements
Before running the project, make sure the following are installed:

- Java JDK 11 or higher
- Maven
- JavaFX SDK 
- IntelliJ IDEA / Eclipse / VS Code (or any IDE of choice)

### Maven Dependencies
The following Maven dependencies are required.
- `javafx-controls`
- `javafx-fxml`
- `org.json`
- `java.net.http`
- `ikonli-javafx`

All dependencies are managed automatically through `pom.xml`.

### API key setup
Create a file named `src/main/resources/API_KEY`.
Paste your API key inside the file.

### From an IDE
1. Open the project in your Java IDE.
2. Make sure JavaFX dependencies are available through Maven.
3. Set the API key.
4. Run the `app.weather.App` class.

## How to Use the App
1. Launch the application. It will automatically try to detect your location.
2. Type a city name into the search field.
3. Click the `Go` button.
4. Use the radio buttons to switch temperature and wind-speed units.
5. Review the history panel to see recent searches with timestamps.

## Implementation
The application follows a MVC approach.

### GUI
The user interface is built with JavaFX using:

- `FXML` for the layout
- `CSS` for styling
- `WeatherController` to connect user actions to the interface

### Data Retrieval
`WeatherService` uses Java's `HttpClient` to:

- request current weather data from OpenWeather
- request forecast data from OpenWeather
- request an approximate default city from an IP geolocation service

### Data Models
The project uses two immutable model classes:

- `WeatherData` for current weather information
- `ForecastData` for forecast entries

### Utility Classes
- `UnitConverter` handles temperature and wind-speed conversions
- `TimeUtils` determines the visual theme based on sunrise, sunset, and
  observation times