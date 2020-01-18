use class_3901;

create table Suppliersorders( 
SuppliersOrdersID int not null auto_increment,
SupplierID varchar(225) not null,
ShippingReferenceID int not null,
OrderDate timestamp,
primary key(SuppliersOrdersID),
constraint FKSupplier foreign key (SupplierID) references suppliers(SupplierID),
constraint FKShippingRef foreign key (ShippingReferenceID) references ShippingReferences(ShippingReferenceID)
);
create table SuppliersOrderdetails( 
SuppliersOrdersDetailsID int not null auto_increment,
SuppliersOrdersID int,
ProductID int,
Quantity int,
UnitPrice timestamp,
primary key(SuppliersOrdersDetailsID),
constraint FKSuppliersorders foreign key (SuppliersOrdersDetailsID) references Suppliersorders(SuppliersOrdersDetailsID)
);
create table ShippingReferences( 
ShippingReferenceID int not null auto_increment,
ShipperID int not null,
CurrentLocation varchar(225),
OrderStatus int,
ArrivalDate timestamp,
primary key(ShippingReferenceID),
constraint FKShippingRef foreign key (ShippingReferenceID) references ShippingReferences(ShippingReferenceID),
constraint FKSupplier foreign key (ShipperID) references shippers(ShipperID)
);

