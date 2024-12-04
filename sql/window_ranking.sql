create database test;
create table emp (
    dt int,
    id varchar(10),
    p_name varchar(10),
    scale dobule
);
insert into emp (dt, id, p_name, scale)
values (20230531, '1', 'a', 0.0694518871940552);
insert into emp (dt, id, p_name, scale)
values (20230531, '1', 'b', 0.0567205048859576);
insert into emp (dt, id, p_name, scale)
values (20230531, '2', 'c', 0.0799003167115256);
insert into emp (dt, id, p_name, scale)
values (20230531, '3', 'd', 0.508498993786544);
insert into emp (dt, id, p_name, scale)
values (20230531, '4', 'e', 0.608378244646523);
insert into emp (dt, id, p_name, scale)
values (20230531, '5', 'f', 0.896150339110864);
insert into emp (dt, id, p_name, scale)
values (20230601, '1', 'b', 0.189279680186868);
insert into emp (dt, id, p_name, scale)
values (20230601, '2', 'c', 0.981130885063262);
insert into emp (dt, id, p_name, scale)
values (20230601, '4', 'e', 0.00340831274990849);
insert into emp (dt, id, p_name, scale)
values (20230601, '4', 'h', 0.418040543511079);
insert into emp (dt, id, p_name, scale)
values (20230601, '5', 'k', 0.58370706607708);
insert into emp (dt, id, p_name, scale)
values (20230601, '6', 'p', 0.811009615716819);

with cte_ranked as (
    select *,
        rank() over (
            partition by id,
            p_name
            order by dt
        ) ranked
    from emp
),
cte_ranked2 as (
    select *,
        min(ranked) over (partition by id) as minid
    from cte_ranked
    where dt = (
            select max(dt)
            from emp
        )
)
select *
from cte_ranked2
where ranked = minid;