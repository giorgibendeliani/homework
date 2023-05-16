create table order_product
(
    quantity   integer not null,
    order_id   bigint  not null,
    product_id bigint  not null,
    primary key (order_id, product_id)
) engine = InnoDB;

create table orders
(
    id     bigint not null auto_increment,
    client varchar(255),
    status varchar(255),
    primary key (id)
) engine = InnoDB;

create table product
(
    id     bigint           not null auto_increment,
    amount integer          not null,
    name   varchar(255)     not null,
    price  double precision not null,
    primary key (id)
) engine = InnoDB;

create table users
(
    id       integer     not null auto_increment,
    email    varchar(50) not null,
    password varchar(64) not null,
    primary key (id)
) engine = InnoDB;

alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

alter table order_product
    add constraint FKl5mnj9n0di7k1v90yxnthkc73 foreign key (order_id) references orders (id);

alter table order_product
    add constraint FKhnfgqyjx3i80qoymrssls3kno foreign key (product_id) references product (id);
