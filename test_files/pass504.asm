  0         JUMP         L14
  1  L10:   PUSH         1
  2         LOADL        9
  3         STORE        3[LB]
  4         LOADL        6
  5         STORE        3[LB]
  6         JUMP         L12
  7  L11:   PUSH         1
  8         LOAD         3[LB]
  9         STORE        4[LB]
 10         LOAD         3[LB]
 11         LOADL        1
 12         CALL         add     
 13         STORE        3[LB]
 14         POP          1
 15         LOAD         3[LB]
 16         LOADL        1
 17         CALL         add     
 18         STORE        3[LB]
 19  L12:   LOAD         3[LB]
 20         LOADL        1500
 21         CALL         lt      
 22         JUMPIF (1)   L11
 23         LOAD         3[LB]
 24         LOADL        1500
 25         CALL         eq      
 26         JUMPIF (0)   L13
 27         LOADL        4
 28         CALL         putint  
 29  L13:   RETURN (0)   0
 30  L14:   LOADL        -1
 31         LOADL        1
 32         LOADA        1[CB]
 33         LOADL        -1
 34         CALL         L10
 35         HALT   (0)   
