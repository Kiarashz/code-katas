with mytable as (
select ticker, market_date, price, volume,
rank() over (partition by ticker order by price desc) as price_rank
from trading.prices
)
select ticker, market_date, price, volume, price_rank
from mytable
where price_rank < 6;

-- how to use range
WITH cte_adjusted_prices AS (
SELECT
  ticker,
  market_date,
  price,
  CASE
    WHEN RIGHT(volume, 1) = 'K' THEN LEFT(volume, LENGTH(volume)-1)::NUMERIC * 1000
    WHEN RIGHT(volume, 1) = 'M' THEN LEFT(volume, LENGTH(volume)-1)::NUMERIC * 1000000
    WHEN volume = '-' THEN 0
  END AS volume
FROM trading.prices
),
cte_moving_averages AS (
  SELECT
    ticker,
    market_date,
    price,
    AVG(price) OVER (
      PARTITION BY ticker
      ORDER BY market_date
      RANGE BETWEEN '7 DAYS' PRECEDING AND CURRENT ROW
    ) AS moving_avg_price,
    volume,
    AVG(volume) OVER (
      PARTITION BY ticker
      ORDER BY market_date
      RANGE BETWEEN '7 DAYS' PRECEDING AND CURRENT ROW
    ) AS moving_avg_volume
  FROM cte_adjusted_prices
)
-- final output
SELECT * FROM cte_moving_averages
WHERE market_date BETWEEN '2021-08-01' AND '2021-08-10'
ORDER BY ticker, market_date;

-- how to sum accumulatively 
WITH cte_monthly_volume AS (
  SELECT
    ticker,
    DATE_TRUNC('MON', market_date)::DATE AS month_start,
    SUM(
      CASE
      WHEN RIGHT(volume, 1) = 'K' THEN LEFT(volume, LENGTH(volume)-1)::NUMERIC * 1000
      WHEN RIGHT(volume, 1) = 'M' THEN LEFT(volume, LENGTH(volume)-1)::NUMERIC * 1000000
      WHEN volume = '-' THEN 0
    END
  ) AS monthly_volume
  FROM trading.prices
  WHERE market_date BETWEEN '2020-01-01' AND '2020-12-31'
  GROUP BY ticker, month_start
)
-- final output
SELECT
  ticker,
  month_start,
  SUM(monthly_volume) OVER (
    PARTITION BY ticker
    ORDER BY month_start
    ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
  ) AS cumulative_monthly_volume
FROM cte_monthly_volume
ORDER BY ticker, month_start;