  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        8
  3         STORE        3[LB]
  4         PUSH         1
  5         LOAD         3[LB]
  6         CALL         newarr  
  7         STORE        4[LB]
  8         PUSH         1
  9         LOAD         3[LB]
 10         STORE        5[LB]
 11         LOAD         5[LB]
 12         CALL         putint  
 13         RETURN (0)   0
 14  L11:   LOADL        -1
 15         LOADL        1
 16         LOADA        1[CB]
 17         LOADL        -1
 18         CALL         L10
 19         HALT   (0)   
