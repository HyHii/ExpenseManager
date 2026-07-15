package com.example.expensemanager.utils

data class CategoryGroup(
    val name: String,
    val subCategories: List<String>
)

val incomeCategoryGroups = listOf(
    CategoryGroup(
        name = "Salary & Allowance",
        subCategories = listOf(
            "Salary",
            "Overtime",
            "Allowance"
        )
    ),
    CategoryGroup(
        name = "Bonus & Commission",
        subCategories = listOf(
            "Monthly / Quarterly / Annual Bonus",
            "Sales Commission"
        )
    ),
    CategoryGroup(
        name = "Side Hustle & Business",
        subCategories = listOf(
            "Freelance / Project Income",
            "Business Income",
            "Selling Used Items"
        )
    ),
    CategoryGroup(
        name = "Investment & Assets",
        subCategories = listOf(
            "Bank Interest / Savings Interest",
            "Dividends / Crypto / Stocks",
            "Rental Income"
        )
    ),
    CategoryGroup(
        name = "Other Income",
        subCategories = listOf(
            "Gifts",
            "Refunds",
            "Other Income"
        )
    )
)

val expenseCategoryGroups = listOf(
    CategoryGroup(
        name = "Food & Drinks",
        subCategories = listOf(
            "Breakfast",
            "Lunch",
            "Dinner",
            "Coffee / Milk Tea",
            "Snacks",
            "Food Delivery"
        )
    ),
    CategoryGroup(
        name = "Transportation",
        subCategories = listOf(
            "Fuel",
            "Parking",
            "Grab / Taxi",
            "Bus Ticket",
            "Vehicle Maintenance"
        )
    ),
    CategoryGroup(
        name = "Shopping",
        subCategories = listOf(
            "Clothes",
            "Cosmetics",
            "Technology",
            "Personal Items",
            "Household Items"
        )
    ),
    CategoryGroup(
        name = "Bills & Living",
        subCategories = listOf(
            "Electricity Bill",
            "Water Bill",
            "Internet",
            "Phone Bill",
            "Rent"
        )
    ),
    CategoryGroup(
        name = "Entertainment",
        subCategories = listOf(
            "Movies",
            "Games",
            "Travel",
            "Going Out",
            "Subscription"
        )
    ),
    CategoryGroup(
        name = "Health & Education",
        subCategories = listOf(
            "Medicine",
            "Medical Checkup",
            "Tuition Fee",
            "Books / Materials",
            "Courses"
        )
    ),
    CategoryGroup(
        name = "Others",
        subCategories = listOf(
            "Gifts",
            "Charity",
            "Other Expenses"
        )
    )
)

val scheduledCategoryGroups = listOf(
    CategoryGroup(
        name = "Housing & Regular Bills",
        subCategories = listOf(
            "Rent",
            "Electricity Bill",
            "Water Bill",
            "Internet",
            "Phone Bill",
            "Apartment / Service Fee"
        )
    ),
    CategoryGroup(
        name = "Subscriptions & Digital Services",
        subCategories = listOf(
            "Netflix / Spotify / YouTube",
            "Cloud Storage",
            "App Subscription",
            "Game Subscription",
            "Study / Work Software"
        )
    ),
    CategoryGroup(
        name = "Study & Work",
        subCategories = listOf(
            "Tuition Fee",
            "Recurring Course Fee",
            "Books / Materials",
            "Work Tools",
            "Exam / Certificate Fee"
        )
    ),
    CategoryGroup(
        name = "Health & Insurance",
        subCategories = listOf(
            "Health Insurance",
            "Vehicle Insurance",
            "Personal Insurance",
            "Regular Medical Checkup",
            "Regular Medicine"
        )
    ),
    CategoryGroup(
        name = "Regular Transportation",
        subCategories = listOf(
            "Monthly Bus Pass",
            "Monthly Parking Fee",
            "Regular Vehicle Maintenance",
            "Estimated Fuel Cost"
        )
    ),
    CategoryGroup(
        name = "Personal Finance",
        subCategories = listOf(
            "Installment Payment",
            "Debt Repayment",
            "Bank Fee",
            "Recurring Savings",
            "Recurring Investment"
        )
    ),
    CategoryGroup(
        name = "Others",
        subCategories = listOf(
            "Other Scheduled Expense",
            "Family",
            "Pet Care",
            "Other Recurring Cost"
        )
    )
)