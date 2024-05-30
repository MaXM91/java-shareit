# java-shareit
### Preamble
A service for sharing things.
### Structure
#### java-shareit.
#### gateway.
#### Users:
- POST /users - add user
- GET /users/{userId} - get user by id
- GET /users - get all users
- PATCH /users/{userId} - update user by id
- DELETE /users/{userId} - delete user by id
#### Item:
- POST /items - add item
- POST /items/{itemId}/comment - add comment on item by id
- GET /items/{itemId} - get item by id
- GET /items - get items by owner id
- GET /items/search - get items by string on page
- PATCH /items/{itemId} - patch item by id
#### ItemRequest:
- POST /requests - add itemRequest
- GET /requests - get all your itemRequests
- GET /requests/{requestId} - get itemRequest by id
- GET /requests/all - get all itemRequests
#### Booking:
- POST /bookings - add booking
- GET /bookings/{bookingId} - get booking by id
- GET /bookings - get bookings by user id on list
- GET /bookings/owner - get booking by owner item
- PATCH /bookings/{bookingId} - change status booking
### Launch
*Without docker*, change the postgresql DB connection settings in the application.properties files  
and deploy the project with the default profile.
db -> server -> gateway.  
*With docker-compose* - change the postgresql DB connection settings in the application.properties  
files and set the necessary docker-compose settings for each container.
