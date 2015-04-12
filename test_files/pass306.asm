  0         JUMP         L11
  1  L10:   LOADL        3
  2         CALL         putint  
  3         RETURN (0)   0
  4  L11:   LOADL        -1
  5         LOADL        1
  6         LOADA        1[CB]
  7         LOADL        -1
  8         CALL         L10
  9         HALT   (0)   
