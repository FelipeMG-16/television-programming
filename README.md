# Television Programming API

This is a Java + Spring Boot application for managing television programming schedules.

## Overview

The Television Programming API allows users to manage television program schedules. It provides endpoints for adding, modifying, and deleting programs, as well as for querying program information.

## Features

- Add a program: Add a new program to the schedule.
- Modify a program: Modify the details of an existing program.
- Delete a program: Delete a program from the schedule.
- Query programs: Retrieve information about all programs or programs on a specific channel.
- Check for schedule overlaps: Ensure that programs do not overlap in schedule time.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL

## Setup

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
3. Set up a PostgreSQL database and update the database configuration in `application.properties`.
4. Run the application.

## Usage

1. Use a tool like Postman to send HTTP requests to the API endpoints.
2. Use the following endpoints:

- `POST /api/program`: Add a new program.
- `PUT /api/program/{id}`: Modify an existing program.
- `DELETE /api/program/{id}`: Delete a program.
- `GET /api/program`: Get information about all programs.
- `GET /api/program/channel/{channelId}`: Get information about programs on a specific channel.
- `GET /api/program/channel/{channelId}/overlaps`: Check for schedule overlaps for programs on a specific channel.

## Examples

Here are some examples of HTTP requests you can send to the API:

- **Add a program:**

```POST http://localhost:8080/api/program
{
"name": "My Program",
"schedule": "2024-05-18T18:00:00",
"day": "Monday",
"duration": 60,
"channelId": 1
}
```


- **Modify a program:**

``` PUT http://localhost:8080/api/program/1
{
"name": "Updated Program",
"schedule": "2024-05-18T19:00:00",
"day": "Monday",
"duration": 90
}
```


- **Delete a program:**
```
DELETE http://localhost:8080/api/program/1
```


- **Get information about all programs:**
```
GET http://localhost:8080/api/program
```


- **Check for schedule overlaps on a specific channel:**
```
GET http://localhost:8080/api/program/channel/1/overlaps?inicio=2024-05-18T18:00:00&fin=2024-05-18T20:00:00

```



## Contributing

Contributions are welcome! If you find any bugs or have suggestions for improvement, please open an issue or create a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
