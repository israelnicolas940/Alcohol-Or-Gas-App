# Fuel Calculator App: Gas or Alcohol?

This is a simple and intuitive Android app developed in Jetpack Compose that helps users decide the best fuel option for their car: Gasoline or Alcohol. The app is designed with accessibility in mind, ensuring that everyone, including users relying on screen readers or other assistive technologies, can easily interact with its features.

## Features

- Fuel Price Input: Enter the prices for both gasoline and alcohol.
- Utilization Rate Switch: Choose between 70% and 75% efficiency rates, depending on your vehicle's engine performance.
- Best Option Calculation: Calculates and displays the better fuel option based on the entered prices and selected utilization rate.
- Accessibility Features:
  - Content descriptions for all interactive components to support screen readers.
  - Proper contrast ratios for better visibility.
  - Dynamic text scaling support to ensure readability for users with larger font settings.
  - Gas Station Management.
    - Create: Add new gas stations with details such as name, address, gasoline price, and alcohol price.
    - Read: View a list of saved gas stations with their details.
    - Update: Edit the details of existing gas stations.
    - Delete: Remove gas stations from the list.
  - Location features.
    - Current Location: Fetch the user's current location to automatically populate the address field when saving a gas station.
    - Open in Maps: View the location of a saved gas station on Google Maps or another map app

## How It Works
### Fuel Calculation
- Enter the price of Gasoline and Alcohol in the respective input fields.
- Toggle the utilization rate switch to choose between 70% and 75% efficiency.
- Tap the calculate button to see which fuel option is more cost-effective.
- The app will display the best choice for fueling your car.
### Gas Station Management
- Add a Gas Station:
  - Navigate to the "Save Gas Station" screen in the '+' icon.
  - Enter the gas station's name, address, gasoline price, and alcohol price.
  - The app can automatically fetch your current location to populate the address field.
  - Save the gas station to the list.

- View Gas Stations:
  - Navigate to the "Gas Station List" screen.
  - View all saved gas stations with their details.
  - Edit a Gas Station:
    - Tap on a gas station in the list to open the "Gas Station Menu".
    - Update the gas station's details and save the changes.

- Delete a Gas Station:
  - Tap the delete icon in the "Gas Station Menu" to remove the gas station from the list.

### Location Features
- Fetch Current Location:
  - When saving a gas station, the app can fetch your current location and use it as the address.

- Open in Maps:
  - Tap the location icon in the Gas station menu to open the gas station's location in Google Maps.
    
## Screenshot

### Fuel Calculation Screen
<img src="https://github.com/user-attachments/assets/e6fb93f6-3015-457b-afc7-416919474521" alt="Fuel Calculation Screen" width="400" height="800">

### Permission Request Screen
<img src="https://github.com/user-attachments/assets/bc565503-e2b7-426e-b189-15aa70170510" alt="Permission Request Screen" width="400" height="800">

### Gas Station List Screen
<img src="https://github.com/user-attachments/assets/cfcaa91a-a86b-479e-934a-1cda9827e34e" alt="Gas Station List Screen" width="400" height="800">

### Save Gas Station Screen
<img src="https://github.com/user-attachments/assets/d2d7df86-05b6-49aa-b406-1e83c87b8d5d" alt="Save Gas Station Screen" width="400" height="800">

### Gas Station Menu Screen
<img src="https://github.com/user-attachments/assets/50ada0f0-fef4-4ce0-bcb2-63b73e32bdee" alt="Gas Station Menu Screen" width="400" height="800">

## Tech Stack

- Jetpack Compose: For building the UI.
- Kotlin: Primary programming language.
- Material 3: For modern and responsive design.
- SharedPreferences: For storing gas station data locally.
- FusedLocationProviderClient: For fetching the user's current location.
- Google Maps Intent: For opening gas station locations in Google Map
