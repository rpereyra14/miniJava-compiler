  0         JUMP         L13
  1  L10:   PUSH         1
  2         LOADL        9
  3         STORE        3[LB]
  4         LOADL        5
  5         STORE        3[LB]
  6         JUMP         L12
  7  L11:   LOAD         3[LB]
  8         CALL         putint  
  9         LOAD         3[LB]
 10         LOADL        1
 11         CALL         add     
 12         STORE        3[LB]
 13  L12:   LOAD         3[LB]
 14         LOADL        6
 15         CALL         lt      
 16         JUMPIF (1)   L11
 17         RETURN (0)   0
 18  L13:   LOADL        -1
 19         LOADL        1
 20         LOADA        1[CB]
 21         LOADL        -1
 22         CALL         L10
 23         HALT   (0)   
