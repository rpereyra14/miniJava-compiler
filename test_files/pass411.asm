  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        2
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        1
  8         LOADA        6[SB]
  9         LOADL        2
 10         CALL         newobj  
 11         CALL         fieldupd
 12         LOAD         3[LB]
 13         LOADL        1
 14         CALL         fieldref
 15         LOADL        1
 16         LOAD         3[LB]
 17         CALL         fieldupd
 18         LOAD         3[LB]
 19         CALL         L12
 20         RETURN (0)   0
 21  L11:   LOADL        -1
 22         LOADL        1
 23         LOADA        1[CB]
 24         JUMP         L13
 25  L12:   PUSH         1
 26         LOADL        10
 27         STORE        4[LB]
 28         LOADA        0[OB]
 29         LOADL        0
 30         LOADL        11
 31         CALL         fieldupd
 32         LOADA        0[OB]
 33         LOADL        1
 34         CALL         fieldref
 35         LOADL        1
 36         CALL         fieldref
 37         LOADL        0
 38         CALL         fieldref
 39         STORE        4[LB]
 40         LOAD         4[LB]
 41         CALL         putint  
 42         RETURN (0)   0
 43  L13:   LOADL        -1
 44         LOADL        1
 45         LOADA        25[CB]
 46         JUMP         L14
 47  L14:   LOADL        -1
 48         LOADL        0
 49         LOADL        -1
 50         CALL         L10
 51         HALT   (0)   
