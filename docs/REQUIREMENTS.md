## Tripter's Functional requeriments

### Description
The application wanted should allow managing of shared expenses between a group of people during a trip.

### Trips
It should allow a user to create a trip, making him the trip's organizer, and add fellow travelers to it. A trip must have a name or title, a starting date and optionally an end date.

### User Registration
Every app's user (both organizers and travelers) must have registered previously. To register, a user must provide first and last name, an email and a password.

### Expenses
A user should be able to add expenses to the trips he/she belongs. Each expense needs to have the total amount charged, the category which describes it best, the users involved and the way in which it will be split up between them, along with how much did each of them pay. It should be allowed to split the expense in the following three ways:
* Equally: every traveller must pay the same amount.
* By Percentages: each of the involved travelers must pay a percentage (which must be specified). The total sum of percentages must be exactly 100%.
* Distributed/by exact values: each of the involved travelers must pay the specified value. The total sum of percentages must be equal to the total cost of the expense.

### Debts
For each trip, a traveler must be able to take a look at how much does he/she owe to others, taking into account all the expenses of that trip.

### Debt payments and loans
A traveller must be able to let the application know when he/she pays his/her debt (either partially or fully) or lends money to another traveler, by giving input of the amount and the traveller that receives the money.
