## 9. Diagrammatic Overview
### 9.1 Ticket Logging Process

```mermaid
graph TD
    A[User Submits Ticket] --> B[Ticket Assigned to Martin]
    B --> |1 Hour to Respond| C[Martin Responds to Ticket]
    B --> |No Response in 1 Hour| D[Ticket Escalated to Alex]
    C --> E[User Receives Response]
    D --> F[Alex Responds to Ticket]
    F --> E[User Receives Response]

