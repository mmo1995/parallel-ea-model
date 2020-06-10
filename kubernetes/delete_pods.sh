kubectl delete deployment coordination-hybrid-deployment --grace-period=0 --force
kubectl delete deployment splitting-joining-hybrid-deployment --grace-period=0 --force
kubectl delete deployment initializer-hybrid-deployment --grace-period=0 --force
#kubectl delete deployment starter-deployment-hybrid --grace-period=0 --force
for island in {1..10}
do
  kubectl delete pod migration-hybrid$island --grace-period=0 --force
  kubectl delete pod ea-master-hybrid$island --grace-period=0 --force
  kubectl delete service ea-master-hybrid$island
  for slave in {1..1}
  do
    kubectl delete pod chromosomeinterpreter-hybrid$island.$slave --grace-period=0 --force
    kubectl delete pod calculation-hybrid$island.$slave --grace-period=0 --force
  done
done
