  0         JUMP         L13
  1  L10:   PUSH         1
  2         LOADL        1
  3         CALL         neg     
  4         STORE        3[LB]
  5         PUSH         1
  6         LOADL        0
  7         STORE        4[LB]
  8         JUMP         L12
  9  L11:   LOAD         4[LB]
 10         LOADL        1
 11         CALL         add     
 12         STORE        4[LB]
 13         LOAD         4[LB]
 14         STORE        3[LB]
 15  L12:   LOAD         4[LB]
 16         LOADL        4
 17         CALL         lt      
 18         JUMPIF (1)   L11
 19         LOAD         3[LB]
 20         CALL         putint  
 21         RETURN (0)   0
 22  L13:   LOADL        -1
 23         LOADL        1
 24         LOADA        1[CB]
 25         LOADL        -1
 26         CALL         L10
 27         HALT   (0)   
