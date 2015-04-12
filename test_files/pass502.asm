  0         JUMP         L14
  1  L10:   PUSH         1
  2         LOADL        8
  3         CALL         neg     
  4         STORE        3[LB]
  5         JUMP         L13
  6  L11:   LOAD         3[LB]
  7         LOADL        2
  8         CALL         eq      
  9         JUMPIF (0)   L12
 10         LOAD         3[LB]
 11         CALL         putint  
 12  L12:   LOAD         3[LB]
 13         LOADL        1
 14         CALL         add     
 15         STORE        3[LB]
 16  L13:   LOAD         3[LB]
 17         LOADL        9
 18         CALL         lt      
 19         JUMPIF (1)   L11
 20         POP          1
 21         RETURN (0)   0
 22  L14:   LOADL        -1
 23         LOADL        1
 24         LOADA        1[CB]
 25         LOADL        -1
 26         CALL         L10
 27         HALT   (0)   
