# Project Overview

The application is currently under development.

The app aims to simplify the process of making decisions together, whether among friends, colleagues, or businesses. It will help users decide on various choices such as where to go, what to eat, and other collaborative decisions. The platform enables users to create, publish, and save lists in an organized format, fostering a space for sharing collections, suggestions, and ideas efficiently. However, its core functionality is to identify common preferences among multiple participants.

Inspired by childhood friendship questionnaires, the app offers an intuitive decision-making tool. Multiple users can interact with lists by marking items using a four-tier system: "Yes," "Rather Yes," "Rather No," and "No." The final selection will only display items that align with all participants' choices. Items that receive conflicting votes (e.g., one user selects "Yes" while another selects "No") will be omitted from the results, preventing discomfort due to differing opinions.

# Technology Stack

## Backend Technologies
- **Spring Boot 3.4.1** - Provides a robust and scalable backend framework.
- **Spring Security** - Manages authentication and authorization with flexible access control.
- **JWT (JSON Web Token)** - Utilized for secure authentication and authorization, leveraging **jjwt-api, jjwt-impl, and jjwt-jackson** (version 0.11.5).

## Database Management
- **PostgreSQL** - Used as the primary relational database.
- **Spring Data JPA & Hibernate** - Combined for efficient data handling; **Spring Data JPA** streamlines repository management and transactions, while **Hibernate** acts as the ORM framework for object-relational mapping.

## Search Functionality
- **Elasticsearch** - Implements full-text search and data indexing.
- **Spring Data Elasticsearch** - Facilitates seamless integration, allowing fast search queries and real-time index updates.

This project is under active development, and more features and enhancements will be introduced in future iterations.

