<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:exslt="http://exslt.org/common"
  xmlns:math="http://exslt.org/math"
  xmlns:str="http://exslt.org/strings"
  xmlns="http://www.w3.org/1999/xhtml">
  <xsl:preserve-space elements="span"/>
  <xsl:variable name="counter">
  <a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/>
  <a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/>
  <a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/>
  <a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/>
  <a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/>
  <a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/>
  <a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/><a/>
  </xsl:variable>
  <xsl:template match="screens">
    <html>
<head>
<style>
.body{font-family: Gill Sans Extrabold, sans-serif;
}

.console {
    background-color: black;
    color:green;
}
.consoleline pre{
	margin:0;
	display:block;
}
.consoleCell{
	padding-bottom:1em;
}
.GRN_RI,WHT_RI,BLU_RI,TRQ_RI,YLW_RI,RED_RI,PNK_RI{
    color:black;
}
.reverseGreenText,.GRN_RI{
	background-color:green;
}
.reverseWhiteText,.WHT_RI{
	background-color:white;
}
.reverseBlueText,.BLU_RI{
	background-color:blue;
}
.reverseTurquoiseText,.TRQ_RI{
	background-color:turquoise;
}
.reverseYellowText,.YLW_RI{
	background-color:yellow;
}
.reverseRedText,.RED_RI{
	background-color:red;
}
.reversePinkText,.PNK_RI{
	background-color:pink;
}

.whiteText,.WHT,.WHT_UL{
	color:white;
}
.blueText,.BLU,.BLU_UL{
	color:SteelBlue;
}
.turquoiseText,.TRQ,.TRQ_UL{
	color:turquoise;
}
.yellowText,.YLW,.YLW_UL{
	color:yellow;
}
.redText,.RED,.RED_UL{
	color:red;
}
.pinkText,.PNK,.PNK_UL{
	color:pink;
}
.underline.WHT_UL,.BLU_UL,.TRQ_UL,.YLW_UL,.RED_UL,.PNK_UL,.GRN_UL{
        text-decoration: underline;
}
.table {
    display:table;
}
.row {
    display:table-row;
	margin-bottom:1em;
	border-bottom:1em;
}
.row:nth-child(odd){
	background-color:#EDF8F6;
}
.cell {
    display:table-cell;
}
.screenInfoBlock{
	margin:1em;
	margin-bottom:0;
	padding:.5em;
	height:100%;
	width:500px;
}

h1,h3{
text-align: center 
}
span {
    white-space: pre;
}
</style>
</head>	
      <body>
<div class="table">
	<xsl:for-each select="screen">
	<xsl:variable name="screen" select="."/>
	<div class="row consoleRow">
		<div class="cell consoleCell">
            <div class="console">
				<xsl:for-each select="exslt:node-set($counter)/*[position()&lt;=$screen/@rows]">
					<xsl:variable name="row" select="position()"/>
					<span class="consoleline">
						<pre>
							<xsl:choose>
								<xsl:when test="$screen/field[@row=$row]">
									<span class="greenText">
										<xsl:for-each select="exslt:node-set($counter)/*[position()&lt; $screen/field[@row=$row][1]/@col]">&#160;</xsl:for-each>
									</span>
									<xsl:for-each select="$screen/field[@row=$row]">
										<xsl:variable name="thisnode" select="."/>
										<xsl:variable name="thispos" select="position()"/>
										<xsl:choose>
											<xsl:when test="$thispos > 1">
												<xsl:variable name="prevnode" select="$thisnode/preceding-sibling::*[1]"/>
												<xsl:variable name="start" select="$prevnode/@col + $prevnode/@length"/>
												
												<span class="greenText">
													<xsl:for-each select="exslt:node-set($counter)/*[position()&gt;=$start and position()&lt;$thisnode/@col]">&#160;</xsl:for-each>
												</span>
											</xsl:when>
										</xsl:choose>
										<xsl:call-template name="renderText"/>
									</xsl:for-each>
									<xsl:variable name="lastnode" select="$screen/field[@row=$row][last()]"/>
									<xsl:variable name="laststart" select="$lastnode/@col + $lastnode/@length - 1"/>
									
									<span class="greenText">
										<xsl:for-each select="exslt:node-set($counter)/*[position()&lt;=$screen/@columns and position() &gt; $laststart]">&#160;</xsl:for-each>
									</span>
								</xsl:when>
								<xsl:otherwise>
									<span class="greenText">
										<xsl:for-each select="exslt:node-set($counter)/*[position()&lt;=$screen/@columns]">&#160;</xsl:for-each>
									</span>
								</xsl:otherwise>
							</xsl:choose>
						</pre>
					</span>
				</xsl:for-each>
			</div>
		</div>
		<div class="cell">
			<div class="screenInfoBlock">
				<xsl:for-each select="notes/note">
					<xsl:value-of select="."/><br/>
				</xsl:for-each>
			</div>
		</div>
	</div>
	</xsl:for-each>

</div>
      </body>
    </html>
  </xsl:template>
  <xsl:template name="renderText">
	<xsl:element name="span">
		<xsl:if test="@attr">
			<xsl:attribute name="class">
				<xsl:value-of select="@attr"/>
			</xsl:attribute>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="@rawText">
				<xsl:value-of select="@rawText"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="@text"/>
				<xsl:if test="@length">
					<xsl:variable name="length" select="@length - string-length(@text)"/>
					<xsl:for-each select="exslt:node-set($counter)/*[position()&lt;=$length]">&#160;</xsl:for-each>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:element>
  </xsl:template>
</xsl:stylesheet>