# Expense Manager App

Expense Manager is a personal finance management Android application.
The app helps users track income, expenses, scheduled payments, budgets, saving goals, and spending analytics in one mobile application.

## Overview

This application is designed to make personal finance tracking simple and convenient. Users can record daily income and expenses, manage future or recurring payments, view financial summaries, analyze spending behavior, and customize app settings.

The app works locally on the device using Room Database and DataStore.

## Main Features

### Dashboard

The Dashboard provides a quick overview of the user's financial status, including:

* Current balance
* Total income
* Total expenses
* Upcoming scheduled expenses
* Predicted balance
* Upcoming payment list

### Income and Expense Tracking

Users can add income and expense records with:

* Title
* Amount
* Category
* Date
* Note

Income and expense categories are separated to keep the data organized and easier to analyze.

### Transaction History

The Transactions screen allows users to:

* View all income and expense records
* Search transactions
* Filter transactions by type
* Edit existing transactions
* Delete transactions

### Scheduled Expenses

Scheduled Expenses are used for future or recurring payments such as:

* Rent
* Internet bills
* Subscriptions
* Insurance
* Loan payments
* Other recurring costs

Users can select a due date using a date picker and set reminder days before the payment date.

### Mark as Paid

The Mark as Paid feature converts a scheduled expense into a real expense transaction after the user has paid it.

After marking a scheduled expense as paid, the app also moves the scheduled item to the next cycle based on its frequency.

### Analytics

The Analytics screen helps users understand their spending behavior through:

* Monthly reports
* Category analysis
* Spending trend charts
* Income vs Expense charts
* Balance trend charts

Chart data is calculated from transaction records stored in the local database. The app groups transactions by date or category, calculates totals, and displays the result using custom Jetpack Compose chart components.

### Goals

The Goals screen helps users track:

* Monthly budget
* Saving goal
* Saving progress
* Saving streak
* Achievement badges

These features are designed to encourage better financial habits.

### Settings

The Settings screen allows users to customize the app, including:

* Dark mode
* Monthly budget
* Saving goal
* Currency setting

The app supports different display currencies while keeping the stored financial data consistent.

## Technology Stack

* Kotlin
* Jetpack Compose
* Room Database
* DataStore
* MVVM Architecture
* Navigation Compose

## Architecture

The app follows the MVVM architecture pattern.

```text
UI Screen
→ ViewModel
→ Repository
→ DAO
→ Room Database
```

### Main Layers

```text
data/local
→ Room entities, DAO files, AppDatabase, DataStore classes

data/repository
→ Repository classes for accessing local data

viewmodel
→ ViewModels for handling UI state and business logic

ui
→ Jetpack Compose screens and reusable components

utils
→ Helper functions, calculators, category data, date formatting, currency formatting
```

## Local Database

The app mainly uses two Room database tables:

### transactions

This table stores actual income and expense records.

Main fields:

* id
* title
* amount
* category
* date
* note
* type

### scheduled_transactions

This table stores future or recurring expenses.

Main fields:

* id
* title
* amount
* category
* note
* type
* frequency
* nextDueDate
* reminderDaysBefore
* isActive

## DataStore

DataStore is used to save user preferences such as:

* Dark mode setting
* Monthly budget
* Saving goal
* Currency setting
* Achievement notification status

## Chart Calculation

Charts are not drawn directly from raw database records.

The app first gets transaction data from Room Database, then processes it using Kotlin calculation functions.

Examples:

```text
Spending Trend:
Filter expense transactions → group by date → sum total expenses per day

Income vs Expense:
Group transactions by date → calculate total income and total expense per day

Balance Trend:
Sort transactions by date → calculate running balance over time

Category Analysis:
Filter expense transactions → group by category → sum amount by category
```

After calculation, the result becomes a list of data points.
These points are passed to custom chart components built with Jetpack Compose.

## Project Structure

```text
ExpenseManager
│
├── data
│   ├── local
│   │   ├── AppDatabase.kt
│   │   ├── TransactionEntity.kt
│   │   ├── TransactionDao.kt
│   │   ├── ScheduledTransactionEntity.kt
│   │   ├── ScheduledTransactionDao.kt
│   │   ├── ThemeDataStore.kt
│   │   ├── BudgetDataStore.kt
│   │   ├── SavingGoalDataStore.kt
│   │   ├── CurrencyDataStore.kt
│   │   └── AchievementDataStore.kt
│   │
│   └── repository
│       ├── TransactionRepository.kt
│       └── ScheduledTransactionRepository.kt
│
├── ui
│   ├── dashboard
│   ├── transaction
│   ├── scheduled
│   ├── analytics
│   ├── goal
│   ├── budget
│   ├── settings
│   ├── components
│   ├── navigation
│   └── theme
│
├── utils
│   ├── CategoryData.kt
│   ├── DateUtils.kt
│   ├── CurrencyFormatUtils.kt
│   ├── ScheduledFrequency.kt
│   ├── ChartDataCalculator.kt
│   ├── ExpenseAnalyticsCalculator.kt
│   ├── MonthlyReportCalculator.kt
│   ├── SpendingTrendCalculator.kt
│   ├── SavingStreakCalculator.kt
│   └── AchievementCalculator.kt
│
├── viewmodel
│   ├── TransactionViewModel.kt
│   ├── ScheduledTransactionViewModel.kt
│   └── ScheduledTransactionViewModelFactory.kt
│
└── MainActivity.kt
```

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/your-username/ExpenseManager.git
```

2. Open the project in Android Studio.

3. Sync Gradle.

4. Run the app on an Android emulator or physical Android device.

## Demo Flow

Recommended demo flow:

```text
Dashboard
→ Add Income
→ Add Expense
→ Transactions
→ Scheduled Expenses
→ Add Scheduled Expense
→ Dashboard Upcoming / Predicted Balance
→ Mark as Paid
→ Analytics
→ Goals
→ Settings
```

## Future Improvements

Possible future enhancements:

* Cloud sync
* User login
* Backup and restore
* Notification reminders
* Export reports
* Bank transaction import
* More advanced financial insights

## Conclusion

Expense Manager provides a complete personal finance management flow in one Android application. Users can record income and expenses, manage scheduled payments, track budgets and saving goals, view analytics, and customize app settings.
