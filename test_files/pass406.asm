  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        2
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        1
  8         LOADA        5[SB]
  9         LOADL        2
 10         CALL         newobj  
 11         CALL         fieldupd
 12         LOAD         3[LB]
 13         LOADL        0
 14         LOADL        5
 15         CALL         fieldupd
 16         LOAD         3[LB]
 17         LOADL        1
 18         CALL         fieldref
 19         LOADL        0
 20         LOADL        1
 21         CALL         fieldupd
 22         PUSH         1
 23         LOAD         3[LB]
 24         LOADL        0
 25         CALL         fieldref
 26         LOAD         3[LB]
 27         LOADL        1
 28         CALL         fieldref
 29         LOADL        0
 30         CALL         fieldref
 31         CALL         add     
 32         STORE        4[LB]
 33         LOAD         4[LB]
 34         CALL         putint  
 35         RETURN (0)   0
 36  L11:   LOADL        -1
 37         LOADL        1
 38         LOADA        1[CB]
 39         JUMP         L12
 40  L12:   LOADL        -1
 41         LOADL        0
 42         JUMP         L13
 43  L13:   LOADL        -1
 44         LOADL        0
 45         LOADL        -1
 46         CALL         L10
 47         HALT   (0)   
