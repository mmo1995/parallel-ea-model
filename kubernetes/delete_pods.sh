kubectl delete deployment coordination-deployment --grace-period=0 --force
kubectl delete deployment container-management-deployment --grace-period=0 --force
kubectl delete deployment splitting-joining-deployment --grace-period=0 --force
kubectl delete deployment initializer-deployment --grace-period=0 --force
for value in {1..1}
do
  kubectl delete pod island-job$value --grace-period=0 --force
  kubectl delete pod island-job-ea$value --grace-period=0 --force
done
