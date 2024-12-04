DROP TABLE IF EXISTS mase;

CREATE TABLE mase (
  loc_id VARCHAR(10),
  wntd_id VARCHAR(10),
  test_type VARCHAR(10),
  predicted_speed INT,
  dt INT
);

INSERT INTO mase VALUES ('L11', 'w10', 'up', 60, 20230508);
INSERT INTO mase VALUES ('L11', 'w10', 'up', 60, 20230509);
INSERT INTO mase VALUES ('L11', 'w10', 'up', 35, 20230510);
INSERT INTO mase VALUES ('L11', 'w10', 'down', 14, 20230510);
INSERT INTO mase VALUES ('L11', 'w10', 'up', 20, 20230511);
INSERT INTO mase VALUES ('L11', 'w10', 'down', 15, 20230511);
INSERT INTO mase VALUES ('L11', 'w10', 'down', 19, 20230512);

INSERT INTO mase VALUES ('L12', 'w15', 'up', 15, 20230509);
INSERT INTO mase VALUES ('L12', 'w15', 'up', 16, 20230510);
INSERT INTO mase VALUES ('L12', 'w15', 'down', 25, 20230510);
INSERT INTO mase VALUES ('L12', 'w15', 'up', 17, 20230511);
INSERT INTO mase VALUES ('L12', 'w15', 'down', 28, 20230511);
INSERT INTO mase VALUES ('L12', 'w15', 'down', 35, 20230512);
INSERT INTO mase VALUES ('L12', 'w15', 'down', 35, 20230513);