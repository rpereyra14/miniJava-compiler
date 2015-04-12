  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        1
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         CALL         L12
  8         LOADL        1
  9         CALL         neg     
 10         LOAD         3[LB]
 11         LOADL        0
 12         CALL         fieldref
 13         CALL         L13
 14         RETURN (0)   0
 15  L11:   LOADL        -1
 16         LOADL        1
 17         LOADA        1[CB]
 18         JUMP         L14
 19  L12:   LOADA        0[OB]
 20         LOADL        0
 21         LOADA        0[OB]
 22         CALL         fieldupd
 23         RETURN (0)   0
 24  L13:   LOAD         -1[LB]
 25         LOADL        27
 26         CALL         add     
 27         CALL         putint  
 28         RETURN (0)   1
 29  L14:   LOADL        -1
 30         LOADL        2
 31         LOADA        19[CB]
 32         LOADA        24[CB]
 33         LOADL        -1
 34         CALL         L10
 35         HALT   (0)   
