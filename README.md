# Banking Management System

A modular Java (Swing + layered architecture) banking management system with:
- OOP modeling (User/Admin/Customer, Account/Savings/Current)
- Device-aware authentication strategies (Android fingerprint, iOS Face ID, Desktop password/PIN simulation)
- In-memory repositories for mock persistence
- Customer and Admin dashboards with account management, transfers, and freeze controls

## Run

```bash
mvn test
mvn exec:java -Dexec.mainClass=com.raidingrain69.bank.BankApplication
```
