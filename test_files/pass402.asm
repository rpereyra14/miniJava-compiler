  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        1
  3         STORE        3[LB]
  4         PUSH         1
  5         LOAD         3[LB]
  6         LOADL        2
  7         LOAD         3[LB]
  8         CALL         mult    
  9         CALL         add     
 10         LOADL        1
 11         CALL         sub     
 12         STORE        4[LB]
 13         LOAD         4[LB]
 14         CALL         putint  
 15         RETURN (0)   0
 16  L11:   LOADL        -1
 17         LOADL        1
 18         LOADA        1[CB]
 19         LOADL        -1
 20         CALL         L10
 21         HALT   (0)   
