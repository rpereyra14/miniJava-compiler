  0         JUMP         L13
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         LOADL        2
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        2
  8         CALL         eq      
  9         JUMPIF (0)   L11
 10         LOADL        3
 11         STORE        3[LB]
 12         JUMP         L12
 13  L11:   LOADL        1
 14         CALL         neg     
 15         STORE        3[LB]
 16  L12:   LOAD         3[LB]
 17         CALL         putint  
 18         RETURN (0)   0
 19  L13:   LOADL        -1
 20         LOADL        1
 21         LOADA        1[CB]
 22         LOADL        -1
 23         CALL         L10
 24         HALT   (0)   
