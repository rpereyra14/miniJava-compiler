  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         PUSH         1
  5         LOADL        4
  6         STORE        4[LB]
  7         PUSH         1
  8         LOAD         4[LB]
  9         CALL         newarr  
 10         STORE        5[LB]
 11         LOAD         4[LB]
 12         LOADL        2
 13         CALL         mult    
 14         STORE        3[LB]
 15         LOAD         3[LB]
 16         CALL         putint  
 17         RETURN (0)   0
 18  L11:   LOADL        -1
 19         LOADL        1
 20         LOADA        1[CB]
 21         LOADL        -1
 22         CALL         L10
 23         HALT   (0)   
