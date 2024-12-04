-- PostgreSQL
select market_date, price, volume, 
  case 
    when (volume like '%K') then cast(regexp_replace(volume, '[KM]', '') as float) * 1000
    when (volume like '%M') then cast(regexp_replace(volume, '[KM]', '') as float) * 1000000
  end as adjusted_volume
from trading.prices
where market_date between '2021-08-01' and '2021-08-10'
order by market_date;

select market_date, price, volume,
  case 
    when right(volume, 1) = 'K' then regexp_replace(volume, '[KM]', '')::numeric * 1000
    when right(volume, 1) = 'M' then regexp_replace(volume, '[KM]', '')::numeric * 1000000
    when right(volume, 1) = '-' then 0
  end as adjusted_volume
from trading.prices
where ticker = 'ETH' and
market_date between '2021-08-01' and '2021-08-10'
order by market_date;