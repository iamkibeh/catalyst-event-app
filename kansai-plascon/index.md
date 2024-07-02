```mermaid
graph TD
    A[User Submits Ticket] --> B[Ticket Assigned to Martin]
    B --> |1 Hour to Respond| C[Martin Responds to Ticket]
    B --> |No Response in 1 Hour| D[Ticket Escalated to Alex]
    C --> E[User Receives Response]
    D --> F[Alex Responds to Ticket]
    F --> E[User Receives Response]
    style A fill:#f9f,stroke:#333,stroke-width:2px
    style B fill:#bbf,stroke:#333,stroke-width:2px
    style C fill:#bff,stroke:#333,stroke-width:2px
    style D fill:#ffb,stroke:#333,stroke-width:2px
    style E fill:#dfd,stroke:#333,stroke-width:2px
    style F fill:#bfb,stroke:#333,stroke-width:2px


