select d.prfx , substr('0'||d.prfx,1,length(h.prfx)),h.prfx from dbn d, hun h where
substr('0'||d.prfx,1,length(h.prfx)) = h.prfx and length(h.prfx) <= length('0'||d.prfx);

select d.prfx , substr(h.prfx,1,length('0'||d.prfx)), h.prfx from dbn d, hun h where
'0'||d.prfx = substr(h.prfx,1,length('0'||d.prfx)) and length(h.prfx) > length('0'||d.prfx);


select prfx from dbn where prfx not in (
    select d.prfx from dbn d, hun h where substr('0'||d.prfx,1,length(h.prfx)) = h.prfx and length(h.prfx) <= length('0'||d.prfx)
    union
    select dd.prfx from dbn dd, hun hh where '0'||dd.prfx = substr(hh.prfx,1,length('0'||dd.prfx)) and length(hh.prfx) > length('0'||dd.prfx)
   );