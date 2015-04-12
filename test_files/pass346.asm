  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         PUSH         1
  5         LOADA        3[SB]
  6         LOADL        2
  7         CALL         newobj  
  8         STORE        4[LB]
  9         LOAD         4[LB]
 10         LOADL        1
 11         LOADA        5[SB]
 12         LOADL        2
 13         CALL         newobj  
 14         CALL         fieldupd
 15         LOAD         4[LB]
 16         LOADL        1
 17         CALL         fieldref
 18         LOADL        0
 19         LOADL        6
 20         CALL         fieldupd
 21         LOAD         4[LB]
 22         LOADL        1
 23         CALL         fieldref
 24         LOADL        0
 25         CALL         fieldref
 26         STORE        3[LB]
 27         LOAD         3[LB]
 28         CALL         putint  
 29         RETURN (0)   0
 30  L11:   LOADL        -1
 31         LOADL        1
 32         LOADA        1[CB]
 33         JUMP         L12
 34  L12:   LOADL        -1
 35         LOADL        0
 36         JUMP         L13
 37  L13:   LOADL        -1
 38         LOADL        0
 39         LOADL        -1
 40         CALL         L10
 41         HALT   (0)   
