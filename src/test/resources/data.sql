truncate user;
truncate club;
truncate hash_tag;
truncate club_hash_tag;
truncate user_club;
truncate user_wish_club;

# listener 영향을 받지 않아서 시간값 null

insert into user(`name`,`student_id`,`password`,`enabled`) values ('jaewon','20171700','112123',true);
insert into user(`name`,`student_id`,`password`,`enabled`) values ('kavin','20171701','2231',true);
insert into user(`name`,`student_id`,`password`,`enabled`) values ('jooeon','20171702','33123',true);
insert into user(`name`,`student_id`,`password`,`enabled`) values ('joohyung','20171703','45123',true);
insert into user(`name`,`student_id`,`password`,`enabled`) values ('david','20171704','51235',true);
insert into user(`name`,`student_id`,`password`,`enabled`) values ('jungho','20171705','521124235',true);

insert into club(`name`,`section`,`recruiting`) values('릴리즈','컴공과',true);
insert into club(`name`,`section`,`recruiting`) values('수영동아리','체육과',true);
insert into club(`name`,`section`,`recruiting`) values('연극동아리','영영과',true);
insert into club(`name`,`section`,`recruiting`) values('불어동아리','프문과',true);
insert into club(`name`,`section`,`recruiting`) values('토론동아리','커뮤과',true);
insert into club(`name`,`section`,`recruiting`) values('명상동아리','철학과',true);

insert into hash_tag(`name`) values('야외');
insert into hash_tag(`name`) values('재밌는');
insert into hash_tag(`name`) values('활발한');
insert into hash_tag(`name`) values('사교적');
insert into hash_tag(`name`) values('내향적');


-- insert into club_hash_tag(`hashTagId`,`clubId`) values(1,2);
-- insert into club_hash_tag(`hashTagId`,`clubId`) values(1,3);
-- insert into club_hash_tag(`hashTagId`,`clubId`) values(1,4);
-- insert into club_hash_tag(`hashTagId`,`clubId`) values(2,5);
-- insert into club_hash_tag(`hashTagId`,`clubId`) values(3,1);
-- insert into club_hash_tag(`hashTagId`,`clubId`) values(3,3);
-- insert into club_hash_tag(`hashTagId`,`clubId`) values(4,2);
-- insert into club_hash_tag(`hashTagId`,`clubId`) values(5,1);
-- insert into club_hash_tag(`hashTagId`,`clubId`) values(5,2);
-- insert into club_hash_tag(`hashTagId`,`clubId`) values(6,4);club



