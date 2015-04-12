  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        2
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        1
  8         LOADA        7[SB]
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
 24         JUMP         L14
 25  L12:   PUSH         1
 26         LOADL        10
 27         STORE        4[LB]
 28         LOADA        0[OB]
 29         LOADL        0
 30         LOADL        4
 31         CALL         fieldupd
 32         LOADL        1
 33         LOADL        3
 34         LOADL        4
 35         LOADA        0[OB]
 36         CALL         L13
 37         CALL         add     
 38         STORE        4[LB]
 39         LOAD         4[LB]
 40         CALL         putint  
 41         RETURN (0)   0
 42  L13:   LOADA        0[OB]
 43         LOADL        0
 44         CALL         fieldref
 45         LOAD         -1[LB]
 46         CALL         add     
 47         LOAD         -2[LB]
 48         CALL         add     
 49         RETURN (1)   2
 50  L14:   LOADL        -1
 51         LOADL        2
 52         LOADA        25[CB]
 53         LOADA        42[CB]
 54         JUMP         L15
 55  L15:   LOADL        -1
 56         LOADL        0
 57         LOADL        -1
 58         CALL         L10
 59         HALT   (0)   
