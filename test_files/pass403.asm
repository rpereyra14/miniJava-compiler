  0         JUMP         L13
  1  L10:   PUSH         1
  2         LOADL        2
  3         STORE        3[LB]
  4         LOAD         3[LB]
  5         LOADL        2
  6         CALL         eq      
  7         JUMPIF (0)   L11
  8         LOADL        3
  9         STORE        3[LB]
 10         JUMP         L12
 11  L11:   LOADL        1
 12         CALL         neg     
 13         STORE        3[LB]
 14  L12:   LOAD         3[LB]
 15         CALL         putint  
 16         RETURN (0)   0
 17  L13:   LOADL        -1
 18         LOADL        1
 19         LOADA        1[CB]
 20         LOADL        -1
 21         CALL         L10
 22         HALT   (0)   
