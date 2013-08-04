
drop table item;
drop table LeadSinger;
drop table HasSong;

drop table item;
create table item 
	(upc int,
	title varchar(50) not null,
	type varchar(3) not null,
	Catagory varchar(12) not null, 
	Company varchar(50) not null,
	year int not null,
	price real not null,
	stock int not null,
	primary key(upc) );

grant select on item to public;

drop table LeadSinger;

create table LeadSinger
	(upc int,
	name varchar(50), 
	primary key (upc, name),
	foreign key (upc) references item(upc));


grant select on LeadSinger to public;


drop table HasSong;

create table HasSong 
     (upc int,
title varchar(50),
primary key (upc, title),
foreign key (upc) references item(upc));

grant select on HasSong to public;