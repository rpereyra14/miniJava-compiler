  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         LOADL        1
  5         STORE        3[LB]
  6         LOADL        2
  7         LOAD         3[LB]
  8         CALL         mult    
  9         LOAD         3[LB]
 10         CALL         add     
 11         LOADL        1
 12         CALL         sub     
 13         STORE        3[LB]
 14         LOAD         3[LB]
 15         CALL         putint  
 16         RETURN (0)   0
 17  L11:   LOADL        -1
 18         LOADL        1
 19         LOADA        1[CB]
 20         LOADL        -1
 21         CALL         L10
 22         HALT   (0)   
