Spring E-Commerce API
Overview
This is a Spring Boot 3-based RESTful API for an e-commerce platform. The API enables basic product management functionality including adding, updating, deleting, retrieving, and searching for products. It also provides the ability to upload and retrieve product images.

Features
Product Management:
Add, update, delete, and retrieve product details such as name, description, price, and category.
Support for managing product stock and availability.
Product Search:
Search for products by keyword (searches product name, description, brand, and category).
Image Handling:
Upload and retrieve product images.
Technologies Used
Spring Boot 3: Framework for building the backend API.
Spring Data JPA: ORM layer to interact with the database.
Lombok: To reduce boilerplate code (e.g., getter/setter, constructors).
H2 Database (or another database of your choice): For persistent storage.
MultipartFile: For handling image uploads.
Endpoints
1. Get All Products
GET /api/products

Response: List of all products.
Status Code: 200 OK
2. Get Product by ID
GET /api/product/{productId}

Path Variable: productId
Response: Product object.
Status Code: 200 OK (If product found), 404 Not Found (If product not found).
3. Get Product Image by ID
GET /api/product/{productId}/image

Path Variable: productId
Response: Product image (byte array).
Status Code: 200 OK (If image found), 404 Not Found (If image or product not found).
4. Add New Product
POST /api/product

Request Body: Product object (name, description, price, etc.) and MultipartFile for image upload.
Response: Created product object.
Status Code: 201 Created, 500 Internal Server Error (if image upload fails).
5. Delete Product by ID
DELETE /api/delete/{productId}

Path Variable: productId
Response: "Deleted" (Success message).
Status Code: 200 OK (If product found and deleted), 404 Not Found (If product not found).
6. Update Product
PUT /api/product

Request Body: Product object (updated details) and MultipartFile for image upload.
Response: "Updated" (Success message).
Status Code: 200 OK, 500 Internal Server Error (if image upload fails).
7. Search Products
GET /api/product/search?keyword={keyword}

Request Parameter: keyword (search term).
Response: List of matching products.
Status Code: 200 OK
