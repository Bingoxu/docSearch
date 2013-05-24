for pickid in `ps -o pid,args -u $USER|grep com.huawei.imax.server.DocServer|grep -v grep|awk '{print $1}'`
do
echo "kill LuceneDocServer ${pickid}"
kill -9 $pickid
done
