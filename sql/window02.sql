with cte_max_speed as (
    select *, 
        max(predicted_speed) over(partition by loc_id, wntd_id, test_type order by test_type) as max_speeds,
        rank() over(partition by loc_id, wntd_id, test_type order by dt desc) as srank
    from mase where test_type in ('up', 'down')
)
select * from cte_max_speed
where srank = 1;
---------------------------------------------------
with cte_max_speeds as (
    select *, 
        max(predicted_speed) over(partition by loc_id, wntd_id, test_type order by test_type) as max_speeds,
        rank() over(partition by loc_id, wntd_id, test_type order by dt desc) as srank
    from mase where test_type in ('up', 'down')
),
cte_max_speeds_per_loc as (
    select * from cte_max_speeds
    where srank = 1;    
)
select *,
    lag(predicted_speed) over(partition by loc_id, wntd_id, test_type order by test_type) as max_up_speed
from cte_max_speeds_per_loc;
------------------------------------------------
with cte_max_speeds as (
    select *, 
        max(predicted_speed) over(partition by loc_id, wntd_id, test_type order by test_type) as max_speeds,
        rank() over(partition by loc_id, wntd_id, test_type order by dt desc) as srank
    from mase where test_type in ('up', 'down')
),
cte_semi_speeds_agg as (
    select *, max_speeds as max_up_speed,     
        lag(predicted_speed) over(partition by loc_id, wntd_id order by test_type) as max_down_speed 
    from cte_max_speeds where srank = 1
)
select loc_id, wntd_id, dt, max_up_speed, max_down_speed from cte_semi_speeds_agg where max_down_speed is not null;
/*
+--------+---------+----------+--------------+----------------+
| loc_id | wntd_id | dt       | max_up_speed | max_down_speed |
+--------+---------+----------+--------------+----------------+
| L11    | w10     | 20230511 |           60 |             19 |
| L12    | w15     | 20230511 |           17 |             35 |
+--------+---------+----------+--------------+----------------+
*/
select * from mase order by loc_id, wntd_id, test_type;