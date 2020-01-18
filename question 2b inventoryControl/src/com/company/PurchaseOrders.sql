use class_3901;
create table Purchaseorder(
PurchaseorderID int not null auto_increment,
SupplierID int not null,
Quantity int,
Price int,
ProductID int,
PurchasedDate date,
ArrivalDate date,
primary key(PurchaseorderID),
constraint FKSupplierRef foreign key (SupplierID) references suppliers(SupplierID),
constraint FKProductID foreign key (ProductID) references products(ProductID)
);
