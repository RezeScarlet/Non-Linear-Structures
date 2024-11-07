#!/bin/bash

echo "Select File"
echo "1 - Menu"
echo "1 - ABB"
echo "2 - AVL"
echo "3 - AVP"
read option
# option=1
if [[ "$option" == "0" ]]; then
  ant -Dnb.internal.action.name=run.single -Djavac.includes=Interface.java -Drun.class=Interface run-single
fi

if [[ "$option" == "1" ]]; then
  ant -Dnb.internal.action.name=run.single -Djavac.includes=SimuladorABB.java -Drun.class=SimuladorABB run-single
fi

if [[ "$option" == "2" ]]; then
  ant -Dnb.internal.action.name=run.single -Djavac.includes=SimuladorAVL.java -Drun.class=SimuladorAVL run-single 
fi

if [[ "$option" == "3" ]]; then
  ant -Dnb.internal.action.name=run.single -Djavac.includes=SimuladorAVP.java -Drun.class=SimuladorAVP run-single
fi
