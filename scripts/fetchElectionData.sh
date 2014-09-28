#!/bin/sh

#Election Campaign
cd /home/ubuntu/richa/ContributionData && { curl -O  ftp://ftp.fec.gov/FEC/[2000-2014]/indiv[00-14].zip; cd-; }
cd /home/ubuntu/richa/ContributionData && { curl -O  ftp://ftp.fec.gov/FEC/[1990-1998]/indiv[90-98].zip; cd-; }

#Committee
cd /home/ubuntu/richa/CommitteData && { curl -O  ftp://ftp.fec.gov/FEC/[2000-2014]/cm[00-14].zip; cd-; }
cd /home/ubuntu/richa/CommitteData && { curl -O  ftp://ftp.fec.gov/FEC/[1990-1998]/cm[90-98].zip; cd-; }

#Candidate
cd /home/ubuntu/richa/CandidateData && { curl -O  ftp://ftp.fec.gov/FEC/[2000-2014]/cn[00-14].zip; cd-; }
cd /home/ubuntu/richa/CandidateData && { curl -O  ftp://ftp.fec.gov/FEC/[1990-1998]/cn[90-98].zip; cd-; }

#Contribution
for f in /home/ubuntu/richa/ContributionData/*.zip; do
dir=${f%.zip}
extn="$dir.txt"
   
#echo $extn
#echo  "${f##*/}"
unzip $f 
mv itcont.txt /home/ubuntu/richa/ContributionData/data/"${extn##*/}" 
done

#Committee
for f in /home/ubuntu/richa/CommitteData/*.zip; do
  echo $f
  dir=${f%.zip}
  extn="$dir.txt"

#echo $extn
 unzip $f
 mv cm.txt /home/ubuntu/richa/CommitteData/data/"${extn##*/}"
done


for f in /home/ubuntu/richa/CandidateData/*.zip; do
  echo $f
   dir=${f%.zip}
   extn="$dir.txt"

#echo $extn
 unzip $f
 mv cn.txt /home/ubuntu/richa/CandidateData/data/"${extn##*/}"
done