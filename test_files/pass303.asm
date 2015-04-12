  0         JUMP         L11
  1  L10:   RETURN (0)   0
  2         LOADA        0[OB]
  3         LOADL        0
  4         LOADL        5
  5         CALL         fieldupd
  6         RETURN (0)   0
  7  L11:   LOADL        -1
  8         LOADL        2
  9         LOADA        1[CB]
 10         LOADA        2[CB]
 11         JUMP         L12
 12  L12:   LOADL        -1
 13         LOADL        0
 14         LOADL        -1
 15         CALL         L10
 16         HALT   (0)   
