  0         JUMP         L13
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         LOADL        6
  5         STORE        3[LB]
  6         JUMP         L12
  7  L11:   PUSH         1
  8         LOADL        1
  9         LOADL        1
 10         CALL         add     
 11         STORE        4[LB]
 12         LOAD         3[LB]
 13         STORE        4[LB]
 14         POP          1
 15         LOAD         3[LB]
 16         LOADL        1
 17         CALL         sub     
 18         STORE        3[LB]
 19  L12:   LOAD         3[LB]
 20         LOADL        1
 21         CALL         gt      
 22         JUMPIF (1)   L11
 23         LOAD         3[LB]
 24         CALL         putint  
 25         RETURN (0)   0
 26  L13:   LOADL        -1
 27         LOADL        1
 28         LOADA        1[CB]
 29         LOADL        -1
 30         CALL         L10
 31         HALT   (0)   
