# --- !Ups

CREATE TABLE "category" (
 "category_id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "category_name" VARCHAR NOT NULL
);
CREATE TABLE "admin" (
 "admin_id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "adminName" VARCHAR NOT NULL,
 "adminPassword" VARCHAR NOT NULL
);

CREATE TABLE "basket" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "user" INTEGER NOT NULL,
 FOREIGN KEY(user) references category(user_id)
);
CREATE TABLE "product" (
 "product_id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL,
 "description" VARCHAR NOT NULL,
 "price" FLOAT NOT NULL,
 "category" INTEGER NOT NULL,
 FOREIGN KEY(category) references category(category_id)
);
CREATE TABLE "user" (
 "user_id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL,
 "email_address" VARCHAR NOT NULL,
 "password" VARCHAR NOT NULL,
 "address" VARCHAR NOT NULL,
 "phone_number" VARCHAR NOT NULL
);
CREATE TABLE "basketProduct" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "basket" INTEGER NOT NULL,
 "product" INTEGER NOT NULL,
 "quantity" INTEGER NOT NULL,
 FOREIGN KEY(product) references product(product_id),
 FOREIGN KEY(basket) references basket(id)
);
CREATE TABLE "order" (
 "order_id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "user_id" INTEGER NOT NULL,
 "status" VARCHAR NOT NULL,
 "date" VARCHAR NOT NULL,
 "payment" FLOAT NOT NULL,
 FOREIGN KEY(user) references user(user_id)
 );
CREATE TABLE "delivery" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL,
 "value" INTEGER NOT NULL,
 "status" INTEGER NOT NULL
);
CREATE TABLE "type" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "product_id" INTEGER NOT NULL,
 "type_field" VARCHAR NOT NULL,
 "date" VARCHAR NOT NULL,
 "payment" FLOAT NOT NULL,
 FOREIGN KEY(product_id) references product(product_id)
);
CREATE TABLE "opinion" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "product_id" INTEGER NOT NULL,
 "text" VARCHAR NOT NULL,
 FOREIGN KEY(product_id) references product(product_id)
);


# --- !Downs


DROP TABLE "category"
DROP TABLE "admin"
DROP TABLE "basket"
DROP TABLE "product"
DROP TABLE "user"
DROP TABLE "basketProduct"
DROP TABLE "order"
DROP TABLE "delivery"
DROP TABLE "type"
DROP TABLE "opinion"